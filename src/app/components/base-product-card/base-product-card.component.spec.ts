import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BaseProductCardComponent } from './base-product-card.component';

describe('BaseProductCardComponent', () => {
  let component: BaseProductCardComponent;
  let fixture: ComponentFixture<BaseProductCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BaseProductCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BaseProductCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
