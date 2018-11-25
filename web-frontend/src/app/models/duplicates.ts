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

import { DuplicatePage } from './comics/duplicate-page';

export interface Duplicates {
  busy: boolean;
  pages: Array<DuplicatePage>;
  hashes: Array<String>;
  pages_by_hash: Map<string, Array<DuplicatePage>>;
  current_duplicates: Array<DuplicatePage>;
  last_hash: string;
  pages_deleted: number;
  pages_undeleted: number;
}
