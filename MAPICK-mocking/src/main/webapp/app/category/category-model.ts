export class CategoryDTO {

  constructor(data:Partial<CategoryDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  name?: string|null;
  description?: string|null;
  categoryImage?: string|null;
  createdAt?: string|null;
  roadmapCategoryRelations?: number|null;

}
