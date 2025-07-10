export class CommentDTO {

  constructor(data:Partial<CommentDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  content?: string|null;
  createdAt?: string|null;
  updatedAt?: string|null;
  map?: number|null;
  member?: number|null;

}
