import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ColaboradorService } from './colaborador.service';
import { Colaborador } from './colaborador';

describe('ColaboradorService', () => {
  let service: ColaboradorService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ColaboradorService]
    });
    service = TestBed.inject(ColaboradorService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should list colaboradores', () => {
    const mock: Colaborador[] = [
      { id: 1, nome: 'Marlon', cpf: '12345678910', dataCafe: '2025-11-21', itensCafe: [] }
    ];

    service.listar().subscribe(data => {
      expect(data.length).toBe(1);
      expect(data[0].nome).toBe('Marlon');
    });

    const req = httpMock.expectOne('http://localhost:8080/api/colaboradores');
    expect(req.request.method).toBe('GET');
    req.flush(mock);
  });

  it('should register a colaborador', () => {
    const novo: Colaborador = {
      nome: 'Amanda', cpf: '98765432100', dataCafe: '2025-11-25',
      itensCafe: []
    };

    service.cadastrar(novo).subscribe(result => {
      expect(result).toBe('Colaborador cadastrado com sucesso');
    });

    const req = httpMock.expectOne('http://localhost:8080/api/colaboradores');
    expect(req.request.method).toBe('POST');
    req.flush('Colaborador cadastrado com sucesso');
  });
});
