import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { PagesRoutingModule } from './pages-routing-module';

import { Cadastro } from './cadastro/cadastro';
import { Lista } from './lista/lista';

@NgModule({
  imports: [
    CommonModule,
    PagesRoutingModule,
    Cadastro,
    Lista
  ]
})
export class PagesModule {}
