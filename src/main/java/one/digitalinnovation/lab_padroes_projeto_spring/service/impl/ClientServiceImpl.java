package one.digitalinnovation.lab_padroes_projeto_spring.service.impl;

import one.digitalinnovation.lab_padroes_projeto_spring.model.Client;
import one.digitalinnovation.lab_padroes_projeto_spring.model.Address;
import one.digitalinnovation.lab_padroes_projeto_spring.repository.ClientRepository;
import one.digitalinnovation.lab_padroes_projeto_spring.repository.AddressRepository;
import one.digitalinnovation.lab_padroes_projeto_spring.service.ClientService;
import one.digitalinnovation.lab_padroes_projeto_spring.service.ViaCepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    ViaCepService viaCepService;

    @Override
    public Iterable<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    public Client findById(Long id) {
        Optional<Client> client = clientRepository.findById(id);
        return client.get();
    }

    @Override
    public void insert(Client client) {
        salvarClienteComCep(client);
    }

    @Override
    public void update(Long id, Client client) {
        Optional<Client> clienteBd = clientRepository.findById(id);
        if(clienteBd.isPresent()){
            salvarClienteComCep(client);
        }
    }

    @Override
    public void delete(Long id) {
        clientRepository.deleteById(id);
    }

    private void salvarClienteComCep(Client client) {
        String cep = client.getAddress().getCep();
        Address address = addressRepository.findById(cep).orElseGet(() -> {
            Address novoAddress = viaCepService.consultarCep(cep);
            addressRepository.save(novoAddress);
            return novoAddress;
        });
        client.setAddress(address);
        clientRepository.save(client);
    }
}
