import { Component } from '@angular/core';
import { CardModule } from 'primeng/card';
import { DrawerModule } from 'primeng/drawer';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  templateUrl: './sidebar.component.html',
  imports: [CardModule, DrawerModule],
})
export class SidebarComponent {
  visible = true;
}
