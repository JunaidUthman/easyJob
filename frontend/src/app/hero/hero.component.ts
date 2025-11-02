import { Component, AfterViewInit, ElementRef, ViewChild } from '@angular/core';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { register } from 'swiper/element/bundle';
import { SwiperOptions } from 'swiper/types';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

// Register Swiper web components
register();

@Component({
  selector: 'app-hero',
  imports: [CommonModule,RouterModule],
  templateUrl: './hero.component.html',
  styleUrl: './hero.component.css',
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class HeroComponent implements AfterViewInit {
  @ViewChild('swiperContainer', { static: false }) swiperContainer!: ElementRef;

  backgroundImages = [
    { src: '/images/hero.png' },
    { src: '/images/apply.jpg' },
    { src: '/images/easy.jpg' },
    { src: '/images/free.jpg' },
    { src: '/images/notification.jpg' }
  ];

  swiperConfig: SwiperOptions = {
    direction: 'horizontal',
    loop: true,
    autoplay: {
      delay: 3000,
      disableOnInteraction: false,
    },
    speed: 1000,
    slidesPerView: 1,
    spaceBetween: 0,
    navigation: false,
    pagination: false,
  };

  ngAfterViewInit() {
    const swiperEl = this.swiperContainer.nativeElement;
    Object.assign(swiperEl, this.swiperConfig);
    swiperEl.initialize();
  }
}

