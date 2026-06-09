import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { UserRegisterDTO } from '../../models/user-register-dto';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-register-form',
  standalone: true,
  imports: [
    ReactiveFormsModule
  ],
  templateUrl: './register-form.component.html',
  styleUrl: './register-form.component.css'
})
export class RegisterFormComponent implements OnInit {
  registerForm!: FormGroup;
  phone: string = '';
  name: string = '';

  constructor(
    private formBuild: FormBuilder,
    private authService: AuthService,
    private userService: UserService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.registerForm = this.formBuild.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      login: ['', [Validators.required, Validators.email]],
      phone: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(11)]],
      adress: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    })
  }

  ngOnInit(): void {

    this.route.queryParams.subscribe(params => {
      if (params['phone']) {
        this.phone = params['phone'];
        this.registerForm.patchValue({ phone: this.phone })
        this.registerForm.get('phone')?.disable();
      }
      if (params['name']) {
        this.name = params['name'];
        this.registerForm.patchValue({ name: this.name })
      }
    })

  }

  onSubmit(): void {
    if (this.registerForm.invalid) {
      alert('Por favor, preencha todos os campos corretamente.');
      return;
    }

    const userData = this.registerForm.getRawValue();
    const userToRegister = { ...userData, role: "USER" } as UserRegisterDTO;

    if (this.phone === '') {
      this.authService.registerUser(userToRegister).subscribe({
        next: (res) => {
          console.log(res.message)
          this.authService.login(userToRegister.login, userToRegister.password).subscribe({
            next: (loginRes) => {
              this.router.navigate(['/home'])
            },
            error: (loginErr) => {
              this.router.navigate(['/login'])
            }
          })
        },
        error: (err) => console.log(err.message)
      });
    } else {
      this.userService.completeProfile(userToRegister).subscribe({
        next: (res) => {
          console.log(res.message)
          this.authService.login(userToRegister.login, userToRegister.password).subscribe({
            next: (loginRes) => {
              this.router.navigate(['/home'])
            },
            error: (loginErr) => {
              this.router.navigate(['/login'])
            }
          })
        },
        error: (err) => console.log(err.message)
      }
      )
    }
  }
}
