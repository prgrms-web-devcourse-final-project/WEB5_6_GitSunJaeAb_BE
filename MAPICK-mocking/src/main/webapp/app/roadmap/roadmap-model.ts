export class MapDTO {

  constructor(data:Partial<MapDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  title?: string|null;
  description?: string|null;
  thumbnail?: string|null;
  isPublic?: boolean|null;
  isAnimated?: boolean|null;
  likeCount?: number|null;
  viewCount?: number|null;
  mapType?: string|null;
  createdAt?: string|null;
  updatedAt?: string|null;
  deletedAt?: string|null;
  member?: number|null;
  originalMap?: number|null;

}
