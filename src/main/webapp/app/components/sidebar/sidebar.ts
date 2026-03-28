import { Component, EventEmitter, Output, ViewChild } from '@angular/core';
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

  @Output() menuSelect = new EventEmitter<'inicio'>();

  @ViewChild('drawerRef') drawerRef?: Drawer;

  selectInicio(event: Event): void {
    event.preventDefault();
    this.menuSelect.emit('inicio');
    this.closeCallback(event);
  }

  closeCallback(event: Event): void {
    if (!this.drawerRef) {
      this.visible = false;
      return;
    }

    this.drawerRef.close(event);
  }
}
