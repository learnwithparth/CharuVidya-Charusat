import { AfterViewInit, Component, ElementRef, ViewChild } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';

import { PasswordResetInitService } from './password-reset-init.service';
import { AlertService } from 'app/core/util/alert.service';

@Component({
  selector: 'jhi-password-reset-init',
  templateUrl: './password-reset-init.component.html',
})
export class PasswordResetInitComponent implements AfterViewInit {
  @ViewChild('email', { static: false })
  email?: ElementRef;

  success = false;
  resetRequestForm = this.fb.group({
    email: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(254), Validators.email]],
  });

  constructor(private passwordResetInitService: PasswordResetInitService, private fb: FormBuilder, private alertService: AlertService) {}

  ngAfterViewInit(): void {
    if (this.email) {
      this.email.nativeElement.focus();
    }
  }

  requestReset(): void {
    this.passwordResetInitService.save(this.resetRequestForm.get(['email'])!.value).subscribe(
      () => (this.success = true),
      () => ((this.success = false), window.alert('Entered email address is not registered!'))
    );
  }
}
