import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, FormArray, Validators, AbstractControl} from '@angular/forms';

import {FileDetails} from '../file-details.model';
import {ComicService} from '../comic.service';

@Component({
  selector: 'app-import-comics',
  templateUrl: './import-comic-list.component.html',
  styleUrls: ['./import-comic-list.component.css']
})
export class ImportComicListComponent implements OnInit {
  directoryForm: FormGroup;
  directory: AbstractControl;
  files: FileDetails[];
  importing = false;
  plural = true;

  constructor(private comicService: ComicService,
    builder: FormBuilder) {
    this.directoryForm = builder.group({'directory': ['', Validators.required]});
    this.directory = this.directoryForm.controls['directory'];
  }

  ngOnInit() {
  }

  onLoad(): void {
    this.getFilesForImport();
  }

  getFilesForImport(): void {
    const directory = this.directory.value;
    console.log('Attempting to get a list of comes from:', directory);
    this.comicService.getFilesUnder(directory).subscribe(
      files => {
        this.files = files;
        this.plural = this.files.length != 1;
      },
      err => {
        console.log(err);
      }
    );
  }

  toggleSelected(file: FileDetails): void {
    file.selected = !file.selected;
  }

  selectAllFiles(): void {
    this.files.forEach((file) => {file.selected = true});
  }

  importFiles(): void {
    this.importing = true;
    const selectedFiles = this.files.filter(file => file.selected).map(file => file.filename);
    console.log(`selectedFiles='${selectedFiles}'`);
    this.comicService.importFiles(selectedFiles).subscribe(
      value => {
        console.log('[POST] response: ', JSON.stringify(value));
        this.importing = false;
        this.getFilesForImport();
      },
      error => {
        console.log('[POST] failed: ', JSON.stringify(error));
        this.importing = false;
      }
    );
  }
}
