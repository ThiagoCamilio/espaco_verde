import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PricingSimulationComponent } from './pricing-simulation.component';

describe('PricingSimulationComponent', () => {
  let component: PricingSimulationComponent;
  let fixture: ComponentFixture<PricingSimulationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PricingSimulationComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PricingSimulationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
