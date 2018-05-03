import {Component, OnInit, Input} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {Router} from '@angular/router';

import {Comic} from '../comic.model';
import {ComicService} from '../comic.service';
import {ComicListComponent} from '../comic-list/comic-list.component';

@Component({
  selector: 'app-comic-list-entry',
  templateUrl: './comic-list-entry.component.html',
  styleUrls: ['./comic-list-entry.component.css']
})

export class ComicListEntryComponent implements OnInit {
  @Input() comic: Comic;
  coverUrl: string;
  showSummary = false;

  constructor(private router: Router, private comicService: ComicService,
    private comicListComponent: ComicListComponent) {}

  ngOnInit() {
    this.coverUrl = this.comicService.getImageUrl(this.comic.id, 0);
  }

  toggleSummary(): void {
    if (this.comic.summary) {
      this.showSummary = !this.showSummary;
    }
  }

  viewComic(): void {
    this.router.navigate([`comics/${this.comic.id}`]);
  }

  deleteComic(): void {
    console.log('Deleting the comic an id of ', this.comic.id, '...');
    this.comicService.deleteComic(this.comic).subscribe(
      success => {
        console.log('success: ', success);
        if (success) {
          console.log('Comic was deleted.');
          this.comicListComponent.getAllComics();
        } else {
          console.log('Comic not delete');
        }
      },
      error => {
        console.log('ERROR: ', error);
      }
    );
  }

}
