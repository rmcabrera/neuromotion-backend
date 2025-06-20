import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NeuromotionDashboardComponent } from './neuromotion-dashboard/neuromotion-dashboard.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, NeuromotionDashboardComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'neuromotion-frontend';
}
