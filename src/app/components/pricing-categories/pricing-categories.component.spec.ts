import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PricingCategoriesComponent } from './pricing-categories.component';

describe('PricingCategoriesComponent', () => {
  let component: PricingCategoriesComponent;
  let fixture: ComponentFixture<PricingCategoriesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PricingCategoriesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PricingCategoriesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
