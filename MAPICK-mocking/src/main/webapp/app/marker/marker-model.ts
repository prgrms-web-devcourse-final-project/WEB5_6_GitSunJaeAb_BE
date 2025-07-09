export class MarkerDTO {

  constructor(data:Partial<MarkerDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  title?: string|null;
  description?: string|null;
  lat?: number|null;
  lng?: number|null;
  color?: string|null;
  imageUrl?: string|null;
  markerSeq?: number|null;
  createdAt?: string|null;
  updatedAt?: string|null;
  deletedAt?: string|null;
  member?: number|null;
  layer?: number|null;

}
