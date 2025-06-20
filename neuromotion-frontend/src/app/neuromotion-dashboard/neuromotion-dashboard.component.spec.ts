import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NeuromotionDashboardComponent } from './neuromotion-dashboard.component';

describe('NeuromotionDashboardComponent', () => {
  let component: NeuromotionDashboardComponent;
  let fixture: ComponentFixture<NeuromotionDashboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NeuromotionDashboardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NeuromotionDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
