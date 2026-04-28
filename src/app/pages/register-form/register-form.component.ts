import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { UserRegisterDTO } from '../../models/user-register-dto';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register-form',
  standalone: true,
  imports: [
    ReactiveFormsModule
  ],
  templateUrl: './register-form.component.html',
  styleUrl: './register-form.component.css'
})
export class RegisterFormComponent implements OnInit{
  registerForm!: FormGroup;

  constructor(private formBuild:FormBuilder, private authService:AuthService, private router:Router){}

  ngOnInit(): void {
    this.registerForm = this.formBuild.group({
      name:['', [Validators.required, Validators.minLength(3)]],
      login:['', [Validators.required, Validators.email]],
      phone:['', [Validators.required, Validators.minLength(10), Validators.maxLength(11)]],
      adress:['', [Validators.required]],
      password:['', [Validators.required, Validators.minLength(6)]]
    })
  }

  onSubmit():void{
    if(this.registerForm.invalid){
      alert('Por favor, preencha todos os campos corretamente.');
      return;
    }

    const userData = this.registerForm.value;
    const userToRegister = {...userData, role:"USER"} as UserRegisterDTO;
    console.log(userToRegister);

    this.authService.registerUser(userToRegister).subscribe({
      next:(res)=> {
        console.log(res.message)
        this.authService.login(userToRegister.login, userToRegister.password).subscribe({
          next:(loginRes) => {
            this.router.navigate(['/home'])
          },
          error:(loginErr) => {
            this.router.navigate(['/login'])
          }
        })
      },
      error:(err)=> console.log(err.message)
    });

  }
}
