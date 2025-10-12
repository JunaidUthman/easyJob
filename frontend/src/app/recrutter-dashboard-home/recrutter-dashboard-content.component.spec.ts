import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecrutterDashboardContentComponent } from './recrutter-dashboard-content.component';

describe('RecrutterDashboardContentComponent', () => {
  let component: RecrutterDashboardContentComponent;
  let fixture: ComponentFixture<RecrutterDashboardContentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RecrutterDashboardContentComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RecrutterDashboardContentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
