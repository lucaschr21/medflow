import { Component, ViewChild } from '@angular/core';
import { AvatarModule } from 'primeng/avatar';
import { ButtonModule } from 'primeng/button';
import type { Drawer } from 'primeng/drawer';
import { DrawerModule } from 'primeng/drawer';
import { RippleModule } from 'primeng/ripple';

@Component({
  selector: 'app-sidebar',
  imports: [AvatarModule, ButtonModule, DrawerModule, RippleModule],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.scss',
})
export class Sidebar {
  visible = false;

  @ViewChild('drawerRef') drawerRef?: Drawer;

  closeCallback(event: Event): void {
    if (!this.drawerRef) {
      this.visible = false;
      return;
    }

    this.drawerRef.close(event);
  }
}
