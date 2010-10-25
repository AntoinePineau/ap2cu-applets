

<head>
<title>Correction d'orthographe via Google</title>
<style>
.texte{ font-family: Arial; font-size: 14px; color: #000000;}
.texte1{ font-family: Arial; font-size: 14px; color: #cc0000;}
.texte2{ font-family: Arial; font-size: 16px; color: #000080; font-weight: bold; text-decoration: underline}.texte2:hover { text-decoration: none; }
</style>
</head>
<?php
$q = "shyqoungounia"; ### mot clé avec une erreur (Chikungunya)
$q_url = urlencode($q);

### recherche la source chez google avec le mot cle erroné
$source = implode ('', file ("http://www.google.fr/search?q=$q_url&hl=fr&lr=lang_fr&sa=N"));

### compte le nombre de 'Essayez avec cette orthographe' dans la source
$nb_proposition = substr_count($source, "Essayez avec cette orthographe");
if($nb_proposition!=0){ # si il y a une proposition, on l'extrait.

### decoupage de $source à 'Essayez avec cette orthographe'
$source = strstr($source, 'Essayez avec cette orthographe');

### decoupage de $source à '<b><i>'
$source = strstr($source, '<b><i>');

$fin_source = strstr($source, '</a>'); # decoupage de $source à '</a>'

### supprimer $fin_source de la chaine $source
$trans = array("$fin_source" => "", "class=spell>" => "", "<b>" => "", "<i>" => "", "</b>" => "", "</i>" => "");
$proposition = strtr($source, $trans);

### formatage de la proposition: urlencode()
$proposition_url = urlencode($proposition);

### affichage du resultat
echo '<a class="texte">Votre recherche : <b>'.$q.'</b></a><br><br><a class="texte1">Essayez avec cette orthographe:
<a class="texte2" href="http://www.ton-site.com/search.php?q=',$proposition_url,'">',$proposition,'</a>';
}
?>

