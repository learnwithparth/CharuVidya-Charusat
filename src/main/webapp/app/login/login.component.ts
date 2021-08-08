import { Component, ViewChild, OnInit, AfterViewInit, ElementRef } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { LoginService } from 'app/login/login.service';
import { AccountService } from 'app/core/auth/account.service';

@Component({
  selector: 'jhi-login',
  templateUrl: './login.component.html',
})
export class LoginComponent implements OnInit, AfterViewInit {
  @ViewChild('username', { static: false })
  username?: ElementRef;

  authenticationError = false;
  errorMessage: string | undefined;

  loginForm = this.fb.group({
    username: [null, [Validators.required]],
    password: [null, [Validators.required]],
    rememberMe: [false],
  });
  invalidLogin = false;

  constructor(
    private accountService: AccountService,
    private loginService: LoginService,
    private router: Router,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    // if already authenticated then navigate to home page
    this.accountService.identity().subscribe(() => {
      if (this.accountService.isAuthenticated()) {
        this.router.navigate(['']);
      }
    });
  }

  ngAfterViewInit(): void {
    if (this.username) {
      this.username.nativeElement.focus();
    }
  }

  login(): void {
    this.loginService
      .login({
        username: this.loginForm.get('username')!.value,
        password: this.loginForm.get('password')!.value,
        rememberMe: this.loginForm.get('rememberMe')!.value,
      })
      .subscribe(
        () => {
          this.authenticationError = false;
          if (!this.router.getCurrentNavigation()) {
            // There were no routing during login (eg from navigationToStoredUrl)

            /**
             * Loading the landing page according to role.
             * */
            if (this.accountService.hasAnyAuthority('ROLE_STUDENT')) {
              this.router.navigate(['enrolled-courses']);
            }
            if (this.accountService.hasAnyAuthority('ROLE_ADMIN')) {
              this.router.navigate(['']);
            }
            if (this.accountService.hasAnyAuthority('ROLE_FACULTY')) {
              this.router.navigate(['instructor-courses']);
            }
            /** * * * * * * * * * * * * * * * * * * * * * * * * * */
          }
        },
        error => this.changeValues(error.error.title)
      );
  }

  private changeValues(error: string): void {
    if (error === 'Incorrect login') {
      this.invalidLogin = true;
      this.authenticationError = false;
    } else {
      this.invalidLogin = false;
      this.authenticationError = true;
    }
  }
}
