import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { VERSION } from 'app/app.constants';
import { AccountService } from 'app/core/auth/account.service';
import { LoginService } from 'app/login/login.service';
import { ProfileService } from 'app/layouts/profiles/profile.service';
import { ActivatedRoute } from '@angular/router';
import { faBookmark } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'jhi-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent implements OnInit {
  inProduction?: boolean;
  isNavbarCollapsed = true;
  openAPIEnabled?: boolean;
  authority = false;
  version = '';
  faBookmark = faBookmark;

  constructor(
    private loginService: LoginService,
    private accountService: AccountService,
    private profileService: ProfileService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    console.warn('Constructor called');
    if (VERSION) {
      this.version = VERSION.toLowerCase().startsWith('v') ? VERSION : 'v' + VERSION;
    }
    this.accountService.getAuthenticationState().subscribe(account => {
      if (account) {
        this.authority = true;
      }
    });
  }

  ngOnInit(): void {
    console.warn('ngOnInit called');
    this.profileService.getProfileInfo().subscribe(profileInfo => {
      this.inProduction = profileInfo.inProduction;
      this.openAPIEnabled = profileInfo.openAPIEnabled;
    });

    this.accountService.getAuthenticationState().subscribe(account => {
      if (account) {
        this.authority = true;
      }
    });
  }

  collapseNavbar(): void {
    this.isNavbarCollapsed = true;
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  logout(): void {
    this.collapseNavbar();
    this.loginService.logout();
    this.authority = false;
    this.router.navigate(['']);
  }

  toggleNavbar(): void {
    this.isNavbarCollapsed = !this.isNavbarCollapsed;
  }

  getImageUrl(): string {
    return this.isAuthenticated() ? this.accountService.getImageUrl() : '';
  }
}
