/*
 * ComixEd - A digital comic book library management application.
 * Copyright (C) 2017, Darryl L. Pierce
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
import {Router} from '@angular/router';

import {Observable} from 'rxjs/Observable';

import {ComicService} from '../comic.service';
import {Page} from '../page.model';
import {Comic} from '../comic.model';

@Component({
  selector: 'app-duplicate-list-entry',
  templateUrl: './duplicate-page-list-entry.component.html',
  styleUrls: ['./duplicate-page-list-entry.component.css']
})
export class DuplicatePageListEntryComponent implements OnInit {
  @Input() page: Page;
  cover_url: string;
  comic: Comic;
  page_title: string;
  delete_page_title: string;
  undelete_page_title: string;
  delete_page_message: string;
  undelete_page_message: string;
  confirm_button = 'Yes';
  cancel_button = 'No';

  constructor(private router: Router, private comicService: ComicService) {}

  ngOnInit() {
    this.cover_url = this.comicService.getImageUrlForId(this.page.id);
    this.comicService.getComicSummary(this.page.comic_id).subscribe(
      (comic: Comic) => {
        this.comic = comic;
        this.page_title = this.page.filename + ' in ' + this.comic.filename + ' [';
        if (this.comic.series) {
          this.page_title = this.page_title + this.comic.series;
        } else {
          this.page_title = this.page_title + 'Unknown Series';
        }
        if (this.comic.issue_number) {
          this.page_title = this.page_title + ' #' + this.comic.issue_number;
        }
        if (this.comic.volume) {
          this.page_title = this.page_title + ' (v' + this.comic.volume + ')';
        }
        this.page_title = this.page_title + ']';
        this.delete_page_title = `Delete the page ${this.page.filename}`;
        this.undelete_page_title = `Undelete the page ${this.page.filename}`;
        this.delete_page_message = 'Are you sure you want to delete this page?';
        this.undelete_page_message = 'Are you sure you want to undelete this page?';
      },
      error => {
        console.log(error);
      }
    );
  }

  openComic(): void {
    this.router.navigate(['comics', this.comic.id]);
  }

  deletePage(): void {
    this.comicService.markPageAsDeleted(this.page)
      .subscribe(response => this.page.deleted = true);
  }

  undeletePage(): void {
    this.comicService.markPageAsUndeleted(this.page)
      .subscribe(response => this.page.deleted = false);
  }
}
