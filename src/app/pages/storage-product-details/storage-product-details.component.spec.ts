import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StorageProductDetailsComponent } from './storage-product-details.component';

describe('StorageProductDetailsComponent', () => {
  let component: StorageProductDetailsComponent;
  let fixture: ComponentFixture<StorageProductDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StorageProductDetailsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StorageProductDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
