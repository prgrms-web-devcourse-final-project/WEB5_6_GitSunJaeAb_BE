export class BookmarkDTO {

  constructor(data:Partial<BookmarkDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  createdAt?: string|null;
  map?: number|null;
  member?: number|null;

}
