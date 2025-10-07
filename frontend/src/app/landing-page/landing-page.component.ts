import { Component } from '@angular/core';
import { HeroComponent } from '../hero/hero.component';
import { InfoComponent } from '../info/info.component';

@Component({
  selector: 'app-landing-page',
  imports: [HeroComponent , InfoComponent],
  templateUrl: './landing-page.component.html',
  styleUrl: './landing-page.component.css'
})
export class LandingPageComponent {

}
