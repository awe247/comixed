/*
 * ComiXed - A digital comic book library management application.
 * Copyright (C) 2017, The ComiXed Project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.package
 * org.comixed;
 */

import {Component, OnInit, Input} from '@angular/core';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';

@Component({
  selector: 'app-page-size',
  templateUrl: './page-size.component.html',
  styleUrls: ['./page-size.component.css']
})
export class PageSizeComponent implements OnInit {
  @Input() page_size: BehaviorSubject<number>;

  protected page_sizes: any[] = [
    {id: 0, label: '10 comics'},
    {id: 1, label: '25 comics'},
    {id: 2, label: '50 comics'},
    {id: 3, label: '100 comics'}
  ];

  constructor() {}

  ngOnInit() {
  }

  set_page_size(size_id: any): void {
    switch (parseInt(size_id, 10)) {
      case 0: this.page_size.next(10); break;
      case 1: this.page_size.next(25); break;
      case 2: this.page_size.next(50); break;
      case 3: this.page_size.next(100); break;
    }
  }
}
