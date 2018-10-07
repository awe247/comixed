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

package org.comixed.library.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name = "roles")
public class Role
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(name = "name",
            updatable = true,
            nullable = false,
            unique = true)
    @JsonView(View.Details.class)
    private String name;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private List<ComiXedUser> users = new ArrayList<>();

    public Long getId()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }

    public List<ComiXedUser> getUsers()
    {
        return this.users;
    }
}
