/*
 * ComiXed - A digital comic book library management application.
 * Copyright (C) 2018, The ComiXed Project
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

import {
  Component,
  OnInit,
  Input,
} from '@angular/core';

import {ComicService} from '../comic.service';
import {AlertService} from '../../alert.service';
import {FileDetails} from '../file-details.model';

@Component({
  selector: 'app-import-comic-list-entry',
  templateUrl: './import-comic-list-entry.component.html',
  styleUrls: ['./import-comic-list-entry.component.css']
})
export class ImportComicListEntryComponent implements OnInit {
  @Input() file_details: FileDetails;
  @Input() cover_size: number;
  file_size: string;
  cover_url: string;
  page_count: number;

  constructor(
    private comic_service: ComicService,
    private alert_service: AlertService,
  ) {}

  ngOnInit() {
    this.cover_url = this.comic_service.get_cover_url_for_file(this.file_details.filename);
    this.file_size = (this.file_details.size / (1024 ** 2)).toPrecision(3).toLocaleLowerCase();
  }
}
