/*
 * Copyright 2019-2020 Google LLC
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA
 */

package com.example.entities;

import com.example.entities.Album.AlbumId;
import com.google.cloud.spanner.hibernate.Interleaved;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.Type;

@Entity
@Interleaved(parentEntity = Singer.class, cascadeDelete = true)
@IdClass(AlbumId.class)
public class Album {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Type(type = "uuid-char")
  private UUID albumId;

  @Id
  @ManyToOne
  @JoinColumn(name = "singerId")
  @Type(type = "uuid-char")
  private Singer singer;

  private String title;

  public Album(Singer singer, String title) {
    this.singer = singer;
    this.title = title;
  }

  public Album() {
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Singer getSinger() {
    return singer;
  }

  public void setSinger(Singer singer) {
    this.singer = singer;
  }

  @Override
  public String toString() {
    return "Album{"
        + "albumId=" + albumId
        + ", singer=" + singer.getName()
        + ", title='" + title + '\''
        + '}';
  }

  /**
   * The embedded key for an entity using the {@link Interleaved} annotation.
   */
  public static class AlbumId implements Serializable {

    // The embedded key of an interleaved table contain all of the Primary Key fields
    // of the parent interleaved entity and be named identically. In this case: singerId.
    Singer singer;

    @Type(type = "uuid-char")
    UUID albumId;

    public AlbumId(Singer singer, UUID albumId) {
      this.singer = singer;
      this.albumId = albumId;
    }

    public AlbumId() {
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      AlbumId albumId1 = (AlbumId) o;
      return Objects.equals(singer, albumId1.singer)
          && Objects.equals(albumId, albumId1.albumId);
    }

    @Override
    public int hashCode() {
      return Objects.hash(singer, albumId);
    }
  }
}
