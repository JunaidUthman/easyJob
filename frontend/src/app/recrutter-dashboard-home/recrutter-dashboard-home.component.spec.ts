import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecrutterDashboardHomeComponent } from './recrutter-dashboard-home.component';

describe('RecrutterDashboardHomeComponent', () => {
  let component: RecrutterDashboardHomeComponent;
  let fixture: ComponentFixture<RecrutterDashboardHomeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RecrutterDashboardHomeComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RecrutterDashboardHomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
