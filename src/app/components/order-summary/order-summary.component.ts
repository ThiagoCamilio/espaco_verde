import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-order-summary',
  standalone: true,
  imports: [
    CommonModule
  ],
  templateUrl: './order-summary.component.html',
  styleUrl: './order-summary.component.css'
})
export class OrderSummaryComponent {

  @Input() totalPrice: number = 0;
  @Input() itemCount: number = 0;
  @Input() buttonText: string = 'Continuar';
  @Input() isButtonDisabled: boolean = false;
  @Input() buttonIcon: string = ''; 
  @Input() showAnalysisWarning: boolean = false;
  @Input() showBackButton: boolean = false;
  @Input() backButtonText: string = 'Voltar';
  
  @Output() actionClick = new EventEmitter<void>();
  @Output() backClick = new EventEmitter<void>();

  handleClick(): void {
    this.actionClick.emit();
  }
  handleBackClick(): void {
    this.backClick.emit();
  }
}
