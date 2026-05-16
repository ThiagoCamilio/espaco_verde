import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { PricingCategory } from '../../models/pricing-category';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-pricing-categories',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './pricing-categories.component.html',
  styleUrl: './pricing-categories.component.css'
})
export class PricingCategoriesComponent implements OnChanges {

  @Input() pricingCategories: PricingCategory[] = [];
  @Output() updatedMargins = new EventEmitter<Record<number, number>>;

  margins: {[id:number]:number} = {};

  ngOnChanges(changes: SimpleChanges): void {
    if(changes['pricingCategories'] && this.pricingCategories){
      this.pricingCategories.forEach(category => {
        this.margins[category.id] = (category.margin || 0) * 100;
      })
    }
  }

  onInputBlur():void{
    const data: Record<number, number> = {};
    for(const id in this.margins){
      if(this.margins.hasOwnProperty(id)){
        data[id] = this.margins[id]/100;
      }
    }
    this.updatedMargins.emit(data);
  }

}
