import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecrutterHeaderComponent } from './recrutter-header.component';

describe('RecrutterHeaderComponent', () => {
  let component: RecrutterHeaderComponent;
  let fixture: ComponentFixture<RecrutterHeaderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RecrutterHeaderComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RecrutterHeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
