import { Component, inject } from '@angular/core';
import { Theme } from '../../core/services/theme';
import { Layout } from '../layout';
import { MatIconButton } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';

@Component({
  selector: 'app-header',
  imports: [MatIconButton, MatIcon],
  templateUrl: './header.html',
  styleUrl: './header.scss',
})
export class Header {
  protected layout = inject(Layout);
  protected theme = inject(Theme);
}
