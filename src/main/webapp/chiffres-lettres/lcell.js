function initMasks() {
  $.mask.rules = {
    '#' : /\d/,
    '+' : /[\+\-\*\/]/,
    'Z' : /[a-zA-Z]/
  };
  $.mask.masks.nombre = {
    mask : '#',
    type : 'repeat',
    maxLength : '5',
    onInvalid : function(c, k) {
      var g = /^operande(\d+)\.(\d+)$/.exec($(this).attr('name'));
      if (!g)
        return;
      if (g[2] == '1' && /^[\+\-\*\/]$/.test(c)) {
        $('input[name=operande' + g[1] + '.' + (parseInt(g[2]) + 1) + ']').focus();
        $('input[name=operation' + g[1] + ']').val(c);
      } else if (g[2] == '2' && c == '=') {
        $('input[name=operande' + g[1] + '.' + (parseInt(g[2]) + 1) + ']').focus();
      }
    }
  };
  $.mask.masks.operation = {
    mask : '+'
  };
  $.mask.masks.mot = {
    mask : 'Z',
    type : 'repeat',
    maxLength : '9'
  };
  $.mask.masks.scrabble = {
      mask : 'Z',
      type : 'repeat',
      maxLength : '11'
    };
}

function nouveauCompteEstBon() {
  $('.resultatChiffres').hide();
  tirageCompteEstBon = reglesCompteEstBon.genererTirage();
  var s = '<span id="resultatAAtteindre">&#160;' + tirageCompteEstBon.getResultatAAtteindre() + '&#160;</span><br/><br/>';
  var lignes = '';

  for (i = 0; i < tirageCompteEstBon.getChiffres().size(); i++) {
    s += '<span class="chiffre">&#160;' + tirageCompteEstBon.getChiffres().get(i) + '&#160;</span>';
    if (i < tirageCompteEstBon.getChiffres().size() - 1) {
      s += "&#160;&#160;&#160;&#160;";
      var ligne = '<tr class="ligne" id="ligne' + i + '">\n';
      ligne += '  <td>\n';
      ligne += '    <input class="operande" type="text" name="operande' + i + '.1" alt="nombre" size="3"></input>&#160;\n';
      ligne += '    <input class="operation" type="text" name="operation' + i + '" alt="operation" size="1"></input>&#160;\n';
      ligne += '    <input class="operande" type="text" name="operande' + i + '.2" alt="nombre" size="3"></input>&#160;\n';
      ligne += '    <span class="operation">&#160;=&#160;</span>&#160;\n';
      ligne += '    <input class="operande" type="text" name="operande' + i + '.3" alt="nombre" size="3"></input><br/>\n';
      ligne += '  </td>\n';
      ligne += '</tr>\n';
      lignes += ligne;
    }
  }
  $('#calcul').html(lignes);
  $('#compteEstBon .tirageChiffres').html(s);

  $('input').setMask();
  $('input[alt=nombre]').keyup(function(e) {
    if (e.keyCode == 13) {
      var g = /^operande(\d+)\.(\d+)$/.exec($(this).attr('name'));
      if (!g)
        return;
      if (g[2] == '2')
        $('input[name=operande' + g[1] + '.' + (parseInt(g[2]) + 1) + ']').focus();
      if (g[2] == '3')
        $('input[name=operande' + (parseInt(g[1]) + 1) + '.1]').focus();
    }
  });

  $('#compteEstBon').show();
  $('#validerCompteEstBon').show();
  $('.ligne0').focus();
}

function validerCalcul() {
  $('#validerCompteEstBon').hide();
  var solution = ap2cu.creerSolutionPourLeCompteEstBon();
  $('#calcul tr[class=ligne]').each(function() {
    var numeroDeLigne = $(this).attr('id').replace(/^ligne(\d+)$/, '$1');

    var ligne = $('input[name="operande' + numeroDeLigne + '.1"]').val();
    ligne += $('input[name="operation' + numeroDeLigne + '"]').val();
    ligne += $('input[name="operande' + numeroDeLigne + '.2"]').val();
    ligne += '=';
    ligne += $('input[name="operande' + numeroDeLigne + '.3"]').val();
    try {
      solution.ajouterLigne(ap2cu.creerLigne(ligne));
    } catch (e) {
    }
  });
  var points = reglesCompteEstBon.attribuerNombreDePoints(solution, tirageCompteEstBon);
  $('.resultatChiffres .points').html("Vous obtenez<br/><span>" + points.getNombreDePoints() + " points</span><br/>(" + points.getJustification() + ")");

  var solutionOrdinateur = tirageCompteEstBon.donnerSolution();
  var html = "L'ordinateur a trouv&#233; ";
  html += solutionOrdinateur.getResultat()==tirageCompteEstBon.getResultatAAtteindre()?"le bon compte":"un compte approch&#233;";
  html += ":<br/><br/><span>";
  var lignes = solutionOrdinateur.getLignes();
  for (i = 0; i < lignes.size(); i++)
    html += lignes.get(i).toString() + '<br/>';
  html+="</span>";
  $('.resultatChiffres .solutionDeLOrdinateur').html(html);
  $('.resultatChiffres').show();
}

function nouveauMotLePlusLong() {
  $('.resultatLettres').hide();
  $('#mot').val('');
  try {
    tirageMotLePlusLong = reglesMotLePlusLong.genererTirage();
    $('#motLePlusLong .tirageLettres').html('&#160;' + tirageMotLePlusLong.getLettres() + '&#160;');

    $('input').setMask();
    $('input[alt=mot]').keyup(function(e) {
      $(this).val($(this).val().toUpperCase());
    });

    $('#motLePlusLong').show();
    $('#validerMotLePlusLong').show();
    $('#mot').focus();
  } catch (e) {
    alert(e);
  }
}

function validerMot() {
  $('#validerMotLePlusLong').hide();
  var solution = ap2cu.creerSolutionPourLeMotLePlusLong($('#mot').val());
  var points = reglesMotLePlusLong.attribuerNombreDePoints(solution, tirageMotLePlusLong);
  $('.resultatLettres .points').html("Vous obtenez <br/><span>" + points.getNombreDePoints() + " points</span><br/>(" + points.getJustification() + ")");
  
  var mot = tirageMotLePlusLong.donnerSolution().getMot();
  var html = "L'ordinateur a trouv&#233; un mot de " + mot.length + " lettres avec:<br/><br/><span>"+mot+"</span>";
  $('.resultatLettres .solutionDeLOrdinateur').html(html);
  $('.resultatLettres').show();
}

function trouverTousLesMots() {
  $('#scrabble').hide();
  var tirageScrabble = ap2cu.creerTirageLettres($('#mot').val());
  var mots = tirageScrabble.trouverTousLesMots();
  var nbLettres = 0;
  var html = '<tr><td><span>';
  for(i=mots.size()-1;i>=0;i--) {
    var mot = mots.get(i);
    if(nbLettres<mot.length) {
      nbLettres=mot.length;
      html += '</span></td><td>&#160;&#160;</td><td><span class="head">Mots de '+nbLettres+' lettres</span><br/><span class="mot">';
    }
    html += mot+'<br/>';
  }
  html += '</span></td></tr>';
  $('#scrabble').html(html);
  $('#scrabble').show();
}