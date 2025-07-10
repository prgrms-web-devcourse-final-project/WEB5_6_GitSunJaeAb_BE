export class LayerDTO {

  constructor(data:Partial<LayerDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  name?: string|null;
  description?: string|null;
  layerSeq?: number|null;
  layerTime?: string|null;
  createdAt?: string|null;
  updatedAt?: string|null;
  deletedAt?: string|null;
  member?: number|null;
  map?: number|null;

}
