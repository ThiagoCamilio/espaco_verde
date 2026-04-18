import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterModule } from '@angular/router';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [ 
    CommonModule,
    RouterModule,
    RouterLink
  ],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css'
})
export class SidebarComponent {

  @Input() isExpanded : Boolean = false;
  @Output() toggle = new EventEmitter<void>();

  toggleSidebar(){
    this.isExpanded = !this.isExpanded;
    this.toggle.emit();
    if(this.isExpanded == true){
      document.documentElement.style.setProperty('--sidebar-width', '160px');
    }else{
      document.documentElement.style.setProperty('--sidebar-width', '80px');
    }
  }

}
