import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { PricingConfig } from '../../models/pricing-config';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-pricing-cost',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './pricing-cost.component.html',
  styleUrl: './pricing-cost.component.css'
})
export class PricingCostComponent implements OnInit{

  @Input() configData!: PricingConfig;
  @Output() newConfig = new EventEmitter<PricingConfig>();

  revenue: number = 0;
  fixExpenses: number = 0;
  variableExpenses: number = 0;

  ngOnInit(): void {
    if(this.configData){
      this.revenue = this.configData.expectedRevenue || 0;
      this.fixExpenses = this.revenue * (this.configData.fixedExpenses || 0);
      this.variableExpenses = (this.configData.variableExpenses || 0) * 100;
    }
  }

  get fixExpensesPercent():number{
    if(this.revenue <= 0 ) return 0;
    return (this.fixExpenses/this.revenue)*100;
  }

  onInputBlur():void{
        
    const updatedConfig : PricingConfig = {
      expectedRevenue : this.revenue,
      fixedExpenses : this.fixExpensesPercent/100,
      variableExpenses : this.variableExpenses/100
    };

    this.newConfig.emit(updatedConfig);
  }
}
