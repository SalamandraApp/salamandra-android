{
  description = "Salamandra App";

  inputs = {
    nixpkgs.url = "github:nixos/nixpkgs/nixos-unstable";
    flake-utils.url = "github:numtide/flake-utils";
  };

  outputs = { self, nixpkgs, flake-utils, ... }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = nixpkgs.legacyPackages.${system};
        nodePackages = pkgs.callPackage ./node-env/default.nix {};
      in {
        devShells.default = pkgs.mkShell {
          nativeBuildInputs = with pkgs; [
            awscli2
            nodePackages."@aws-amplify/cli"
          ];

          shellHook = ''
            echo -e "Entering \e[1;31mSALAMANDRA-APP SHELL\e[0m"
            echo -e " - AWS CLI"
            echo -e " - AWS Amplify CLI"
          '';
        };
      });
}
