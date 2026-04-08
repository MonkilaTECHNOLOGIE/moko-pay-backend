package com.monkila_tech.mokopay_backend.services;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.monkila_tech.mokopay_backend.models.Facture;
import com.monkila_tech.mokopay_backend.repository.FactureRepository;

@Service
public class FactureServiceImpl implements FactureService {
    @Autowired
    private FactureRepository factureRepository;

    @Override
    public Facture saveFacture(Facture facture) throws Exception {
        return factureRepository.save(facture);
    }

    @Override
    public List<Facture> fetchFactureList() throws Exception {
        return (List<Facture>) factureRepository.findAll();
    }


    @Override
    public Facture payeFacture(Facture facture) throws Exception {

        Facture factureDB = factureRepository.findById(facture.getId())
                .get();

        if (Objects.nonNull(facture.getPayee())) {
            factureDB.setPayee(facture.getPayee());
        }

        return factureRepository.save(factureDB);
    }

    @Override
    public Facture getFactureById(Long factureId) throws Exception {
        return factureRepository.findById(factureId).get();
    }

    @Override
    public Boolean deleteFactureById(Long factureId) throws Exception {

        Optional<Facture> facture = this.factureRepository.findById(factureId);

        if (facture.isEmpty())
            return false;

        this.factureRepository.deleteById(factureId);

        Optional<Facture> factureChecked = this.factureRepository.findById(factureId);

        if (factureChecked.isEmpty())
            return true;
        return false;

    }

    @Override
    public List<Facture> fetchFacturePaye() throws Exception {
        return this.factureRepository.findFacturePayee().get();
    }

    @Override
    public List<Facture> fetchFactureListByUserId(Long userId) throws Exception {
        return this.factureRepository.findByFactureByUserId(userId);
    }

    @Override
    public List<Facture> fetchFactureByDate() throws Exception {
        return this.factureRepository.findByDate().get();
    }

    @Override
    public List<Facture> fetchFactureByDateDebutFin(Date dateDebut, Date dateFin) throws Exception {
       return this.factureRepository.findByDateEmissionBetween(dateDebut, dateFin);
    }

}
