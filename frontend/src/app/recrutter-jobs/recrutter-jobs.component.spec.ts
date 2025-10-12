import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecrutterJobsComponent } from './recrutter-jobs.component';

describe('RecrutterJobsComponent', () => {
  let component: RecrutterJobsComponent;
  let fixture: ComponentFixture<RecrutterJobsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RecrutterJobsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RecrutterJobsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
