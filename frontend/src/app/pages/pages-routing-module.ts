import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { Cadastro } from './cadastro/cadastro';
import { Lista } from './lista/lista';

const routes: Routes = [
  { path: 'cadastro', component: Cadastro },
  { path: 'lista', component: Lista },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PagesRoutingModule { }
