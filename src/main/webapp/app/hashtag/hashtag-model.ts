export class HashtagDTO {

  constructor(data:Partial<HashtagDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  name?: string|null;
  createdAt?: string|null;

}
