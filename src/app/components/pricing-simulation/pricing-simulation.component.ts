import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-pricing-simulation',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './pricing-simulation.component.html',
  styleUrl: './pricing-simulation.component.css'
})
export class PricingSimulationComponent {

  @Output() applyMethod = new EventEmitter<string>();
  
  selectedMethod: string = "MARKUP_DIVISOR";

  methods = [
    {value: "MARKUP_DIVISOR", label: "Markup divisor (Margem real)"},
    {value: "LUCRO_SOBRE_O_CUSTO_COM_REPASSE", label: "Lucro sobre o custo com repasse"}
  ]

  mockProducts = [
    { nome: 'Vaso de Terracota G', custo: 45.00, precoBase: 58.50, precoFinal: 90.00, margem: 25 },
    { nome: 'Muda de Jabuticaba', custo: 120.00, precoBase: 156.00, precoFinal: 240.00, margem: 30 },
    { nome: 'Adubo Orgânico 5kg', custo: 25.00, precoBase: 32.50, precoFinal: 45.00, margem: 15 },
    { nome: 'Tesoura de Poda Profissional', custo: 85.00, precoBase: 110.50, precoFinal: 170.00, margem: 25 }
  ];

  onApply():void{
    if(this.selectedMethod){
      this.applyMethod.emit(this.selectedMethod);
    }
  }

}
