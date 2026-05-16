import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PricingCostComponent } from './pricing-cost.component';

describe('PricingCostComponent', () => {
  let component: PricingCostComponent;
  let fixture: ComponentFixture<PricingCostComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PricingCostComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PricingCostComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
