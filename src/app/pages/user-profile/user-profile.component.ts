import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    NgIf
  ],
  templateUrl: './user-profile.component.html',
  styleUrl: './user-profile.component.css'
})
export class UserProfileComponent implements OnInit{

  profileForm!: FormGroup
  loading = false;
  message = false;
  isEditing: boolean = false;
  originalUser: any;


  constructor (private formBuild:FormBuilder, private userService:UserService){}

  ngOnInit(): void {
    
    this.profileForm = this.formBuild.group({
      name:['', Validators.required],
      login:[''],
      phone:['', [Validators.required, Validators.minLength(10)]],
      adress:['', Validators.required]
    })

    this.profileForm.disable();
    this.loadData()

  }

  loadData():void {
    this.userService.getProfile().subscribe({
      next:(res)=>{
        this.profileForm.patchValue(res);
        this.originalUser = this.profileForm.getRawValue();
      },
      error:(err)=>{
        console.log("erro ao carregar dados" , err)
      }
    })
  }

  onSubmit():void{
    if(this.profileForm.invalid){
      return
    }

    this.loading = true;
    this.message = false;

    const updateData = this.profileForm.getRawValue();

    this.userService.updateProfile(updateData).subscribe({
      next:(res) =>{
        this.loading = false;
        this.message = true;
        this.isEditing = false
        this.profileForm.disable();
        this.originalUser = updateData;
        setTimeout(() => this.message = false, 100);
      },
      error: (err) => {
        this.loading = false;
        alert('Erro ao atualizar o perfil. Tente novamente.');
        console.error(err);
      }
    })
  }

  onEditToggle():void{
    this.isEditing = true;
    this.profileForm.enable();
    this.profileForm.get('login')?.disable();
  }

  cancelEdit():void{
    this.isEditing = false;
    this.profileForm.patchValue(this.originalUser);
    this.profileForm.disable();
  }
}
