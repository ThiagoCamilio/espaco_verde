import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BannerService {


  showBanner = new BehaviorSubject<boolean>(true);
  constructor() { }
}
