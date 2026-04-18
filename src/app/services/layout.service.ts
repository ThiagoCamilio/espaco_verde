import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LayoutService {

  constructor() { }

  private sourceTitle = new BehaviorSubject<string>('home');
  title$ = this.sourceTitle.asObservable();
  
  private sourceText = new BehaviorSubject<string>('text');
  text$ = this.sourceText.asObservable();

  private sourceBtnText = new BehaviorSubject<string>('btn');
  btnText$ = this.sourceBtnText.asObservable();

  updateBannerText(newTitle: string, newText: string, newBtnText: string){
    this.sourceTitle.next(newTitle);
    this.sourceText.next(newText);
    this.sourceBtnText.next(newBtnText);
  }

}
