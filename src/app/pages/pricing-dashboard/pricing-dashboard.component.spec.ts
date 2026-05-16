import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PricingDashboardComponent } from './pricing-dashboard.component';

describe('PricingDashboardComponent', () => {
  let component: PricingDashboardComponent;
  let fixture: ComponentFixture<PricingDashboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PricingDashboardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PricingDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
