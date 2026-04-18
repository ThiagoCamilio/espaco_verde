import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StorageProductCardComponent } from './storage-product-card.component';

describe('StorageProductCardComponent', () => {
  let component: StorageProductCardComponent;
  let fixture: ComponentFixture<StorageProductCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StorageProductCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StorageProductCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
