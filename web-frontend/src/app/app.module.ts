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

import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgxPaginationModule } from 'ngx-pagination';
import { MenubarModule } from 'primeng/menubar';
import { SidebarModule } from 'primeng/sidebar';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { CheckboxModule } from 'primeng/checkbox';
import { DropdownModule } from 'primeng/dropdown';
import { SliderModule } from 'primeng/slider';
import { TabViewModule } from 'primeng/tabview';
import { TableModule } from 'primeng/table';
import { ToastModule } from 'primeng/toast';
import { CardModule } from 'primeng/card';
import { DataViewModule } from 'primeng/dataview';
import { DialogModule } from 'primeng/dialog';
import { ScrollPanelModule } from 'primeng/scrollpanel';
import { ToggleButtonModule } from 'primeng/togglebutton';
import { PanelModule } from 'primeng/panel';
import { TooltipModule } from 'primeng/tooltip';
import { ToolbarModule } from 'primeng/toolbar';
import { SplitButtonModule } from 'primeng/splitbutton';
import { ProgressBarModule } from 'primeng/progressbar';
import { BlockUIModule } from 'primeng/blockui';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService } from 'primeng/api';
import { PasswordModule } from 'primeng/password';
import { PickListModule } from 'primeng/picklist';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { LoadingModule, ANIMATION_TYPES } from 'ngx-loading';
import { XhrInterceptor } from './xhr.interceptor';
import { ComicService } from './services/comic.service';
import { MessageService } from 'primeng/api';
import { ComicDetailsComponent } from './ui/pages/comic/comic-details/comic-details.component';
import { ComicReaderComponent } from './ui/components/comic/comic-reader/comic-reader.component';
import { ComicDetailsEditorComponent } from './ui/components/comic/comic-details-editor/comic-details-editor.component';
import { ComicOverviewComponent } from './ui/components/comic/comic-overview/comic-overview.component';
import { ComicStoryComponent } from './ui/components/comic/comic-story/comic-story.component';
import { ComicCreditsComponent } from './ui/components/comic/comic-credits/comic-credits.component';
import { ComicPagesComponent } from './ui/components/comic/comic-pages/comic-pages.component';
import { MainPageComponent } from './ui/pages/main-page/main-page.component';
import { LoginComponent } from './ui/components/login/login.component';
import { AlertService } from './services/alert.service';
import { UserService } from './services/user.service';
import { MenubarComponent } from './ui/components/menubar/menubar.component';
import { LibraryPageComponent } from './ui/pages/library/library-page/library-page.component';
import { NotificationsComponent } from './ui/components/notifications/notifications.component';
import { ImportPageComponent } from './ui/pages/library/import-page/import-page.component';
import { DuplicatesPageComponent } from './ui/pages/library/duplicates-page/duplicates-page.component';
import { ImportToolbarComponent } from './ui/components/import/import-toolbar/import-toolbar.component';
import { SelectedComicsComponent } from './ui/components/import/selected-comics/selected-comics.component';
import { FileDetailsCoverComponent } from './ui/components/file-details/file-details-cover/file-details-cover.component';
import { StoreModule } from '@ngrx/store';
import { userReducer } from './reducers/user.reducer';
import { importingReducer } from './reducers/importing.reducer';
import { libraryReducer } from './reducers/library.reducer';
import { libraryDisplayReducer } from './reducers/library-display.reducer';
import { singleComicScrapingReducer } from './reducers/single-comic-scraping.reducer';
import { multipleComicsScrapingReducer } from './reducers/multiple-comics-scraping.reducer';
import { duplicatesReducer } from './reducers/duplicates.reducer';
import { LibraryCoversComponent } from './ui/components/library/library-covers/library-covers.component';
import { LibraryDetailsComponent } from './ui/components/library/library-details/library-details.component';
import { IssueDetailsComponent } from './ui/components/library/issue-details/issue-details.component';
import { EffectsModule } from '@ngrx/effects';
import { UserEffects } from './effects/user.effects';
import { ImportingEffects } from './effects/importing.effects';
import { LibraryEffects } from './effects/library.effects';
import { SingleComicScrapingEffects } from './effects/single-comic-scraping.effects';
import { DuplicatesEffects } from './effects/duplicates.effects';
import { DuplicatePagesViewComponent } from './ui/views/library/duplicate-pages-view/duplicate-pages-view.component';
import { PageHashViewComponent } from './ui/views/library/page-hash-view/page-hash-view.component';
import { VolumeListComponent } from './ui/components/scraping/volume-list/volume-list.component';
import { TokenStorage } from './storage/token.storage';
import { AccountPageComponent } from './ui/pages/account/account-page/account-page.component';
import { AccountPreferencesComponent } from './ui/components/account/account-preferences/account-preferences.component';
import { UserDetailsComponent } from './ui/components/account/user-details/user-details.component';
import { MultipleComicScrapingComponent } from './ui/components/scraping/multiple-comic-scraping/multiple-comic-scraping.component';
import { LibraryScrapingToolbarComponent } from './ui/components/library/library-scraping-toolbar/library-scraping-toolbar.component';
import { LibraryScrapingSelectionComponent } from './ui/components/library/library-scraping-selection/library-scraping-selection.component';
import { LibraryScrapingViewComponent } from './ui/views/library/library-scraping-view/library-scraping-view.component';
import { ComicListComponent } from './ui/components/library/comic-list/comic-list.component';
import { AdminGuard } from './admin.guard';

@NgModule({
  declarations: [
    AppComponent,
    MainPageComponent,
    LoginComponent,
    MenubarComponent,
    ComicDetailsComponent,
    ComicReaderComponent,
    ComicDetailsEditorComponent,
    ComicOverviewComponent,
    ComicStoryComponent,
    ComicCreditsComponent,
    ComicPagesComponent,
    LibraryPageComponent,
    NotificationsComponent,
    ImportPageComponent,
    DuplicatesPageComponent,
    ImportToolbarComponent,
    SelectedComicsComponent,
    FileDetailsCoverComponent,
    LibraryCoversComponent,
    LibraryDetailsComponent,
    IssueDetailsComponent,
    DuplicatePagesViewComponent,
    PageHashViewComponent,
    VolumeListComponent,
    AccountPageComponent,
    AccountPreferencesComponent,
    UserDetailsComponent,
    MultipleComicScrapingComponent,
    LibraryScrapingToolbarComponent,
    LibraryScrapingSelectionComponent,
    LibraryScrapingViewComponent,
    ComicListComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    HttpClientModule,
    MenubarModule,
    SidebarModule,
    InputTextModule,
    ButtonModule,
    CheckboxModule,
    DropdownModule,
    SliderModule,
    TabViewModule,
    TableModule,
    ToastModule,
    CardModule,
    DataViewModule,
    DialogModule,
    ScrollPanelModule,
    ToggleButtonModule,
    PanelModule,
    TooltipModule,
    ToolbarModule,
    SplitButtonModule,
    ProgressBarModule,
    BlockUIModule,
    ConfirmDialogModule,
    PasswordModule,
    PickListModule,
    LoadingModule.forRoot({
      animationType: ANIMATION_TYPES.pulse,
      fullScreenBackdrop: true,
    }),
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    NgxPaginationModule,

    FormsModule,
    ReactiveFormsModule,
    LoadingModule.forRoot({
      animationType: ANIMATION_TYPES.pulse,
      fullScreenBackdrop: true,
    }),

    StoreModule.forRoot({
      user: userReducer,
      importing: importingReducer,
      library: libraryReducer,
      library_display: libraryDisplayReducer,
      single_comic_scraping: singleComicScrapingReducer,
      multiple_comic_scraping: multipleComicsScrapingReducer,
      duplicates: duplicatesReducer,
    }),
    EffectsModule.forRoot([
      UserEffects,
      ImportingEffects,
      LibraryEffects,
      SingleComicScrapingEffects,
      DuplicatesEffects,
    ]),
  ],
  providers: [
    AlertService,
    UserService,
    ComicService,
    MessageService,
    [
      { provide: HTTP_INTERCEPTORS, useClass: XhrInterceptor, multi: true }
    ],
    ConfirmationService,
    TokenStorage,
    AdminGuard,
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
