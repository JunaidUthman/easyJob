import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecrutterDashboardComponent } from './recrutter-dashboard.component';

describe('RecrutterDashboardComponent', () => {
  let component: RecrutterDashboardComponent;
  let fixture: ComponentFixture<RecrutterDashboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RecrutterDashboardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RecrutterDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
