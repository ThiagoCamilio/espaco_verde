import { Component, OnInit } from '@angular/core';
import { PricingConfig } from '../../models/pricing-config';
import { PricingCategory } from '../../models/pricing-category';
import { PricingService } from '../../services/pricing.service';
import { ToastrService } from 'ngx-toastr';
import { CommonModule } from '@angular/common';
import { PricingCategoriesComponent } from '../../components/pricing-categories/pricing-categories.component';
import { PricingCostComponent } from '../../components/pricing-cost/pricing-cost.component';
import { PricingSimulationComponent } from '../../components/pricing-simulation/pricing-simulation.component';

@Component({
  selector: 'app-pricing-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    PricingCategoriesComponent,
    PricingCostComponent,
    PricingSimulationComponent
  ],
  templateUrl: './pricing-dashboard.component.html',
  styleUrl: './pricing-dashboard.component.css'
})
export class PricingDashboardComponent implements OnInit {

  pricingConfig: PricingConfig | null = null;
  pricingCategories: PricingCategory[] = []

  constructor(private pricingService: PricingService, private toastr: ToastrService) { }

  ngOnInit(): void {
    this.loadConfigs();
  }

  loadConfigs() {
    this.pricingService.getConfig().subscribe(res => this.pricingConfig = res);
    this.pricingService.getCategories().subscribe(res => this.pricingCategories = res);
  }

  onConfigChange(newConfig: PricingConfig): void {
    this.pricingService.updateConfig(newConfig).subscribe(() => {
      this.toastr.success('Custos atualizados!', 'Sucesso');
      this.pricingConfig = newConfig;
    })
  }

  onCategoryChange(newMargin: Record<number, number>): void {
    this.pricingService.updateCategories(newMargin).subscribe(() => {
      this.toastr.success('Margem de lucro atualizada!', 'Sucesso')
      this.loadConfigs();
    })
  }

  onCalculate(method: string): void {
    this.pricingService.calculatePrices(method).subscribe({
      next: (res) => {
        this.toastr.success(res.message, 'Simulação concluida')
      },
      error:(err) => {
        this.toastr.error(err.message, 'Simulação concluida')
      },
    })
  }

}
